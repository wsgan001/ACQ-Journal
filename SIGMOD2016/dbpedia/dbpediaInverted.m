set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x = [1, 2, 3, 4, 5];
y1=[2133.174209	1453.292591	1052.577481	942.5157972	1001.541422];
y2=[1055.989717	1030.605853	837.7028756	827.7889401	807.1749795];
y3 = [209330.06	116771.05	79600.13	51277.85	49063.52];
y4 = [48441.86	41805.37	42355.33	33853.85	38032.43];


p1= semilogy(x, y1, '-ks');
hold on;
p2 = semilogy(x, y2, '-kd');
hold on;
p3 = semilogy(x, y3, '-kv');
hold on;
p4 = semilogy(x, y4, '-k^');
hold on;

xlabel('k');
ylabel('time (ms)');

axis([0.5 5.5 0.0 500000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
%leg=legend('basic-g','basic-w','Inc-S','Inc-T','Dec', 1);
%set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend([p1, p2],'Inc-S','Inc-T');
set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,[p3, p4],'Inc-S*','Inc-T*');
set(leg2,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);


